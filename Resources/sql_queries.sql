SET @bucket_size = 3; -- Set the bucket size here (number of unique delivery dates per bucket)

WITH subquery AS (
    SELECT
        od.Order_No,
        od.Order_Width,
        od.Grade,
        od.Delivery_Date,
        od.BTR_Qty,
        od.BTR_Qty * 1.1 AS BTC_Qty, -- Calculate BTC_Qty as BTR_Qty * 1.1
        od.Product,
        gd.`Grade Grp`,
        CONCAT('L1G', DENSE_RANK() OVER (ORDER BY CAST(SUBSTRING_INDEX(gd.`Grade Grp`, 'AG', -1) AS UNSIGNED))) AS L1_Group, -- Assign L1_Group based on numeric part of Grade Grp
        FLOOR((DENSE_RANK() OVER (ORDER BY od.Delivery_Date) - 1) / @bucket_size) AS bucket,
        COUNT(*) OVER () AS total_records,
        DENSE_RANK() OVER (PARTITION BY CAST(SUBSTRING_INDEX(gd.`Grade Grp`, 'AG', -1) AS UNSIGNED) ORDER BY od.Order_Width DESC) AS width_rank,
        CASE
            WHEN od.Order_Width % 25 = 0 THEN od.Order_Width
            ELSE CEILING(od.Order_Width / 25) * 25
        END AS Ceiling_Order_Width -- Calculate Ceiling_Order_Width
    FROM (
        SELECT DISTINCT
            Order_No,
            Order_Width,
            Grade,
            Delivery_Date,
            BTR_Qty,
            Product
        FROM OrderDetail
    ) od
    LEFT JOIN GradeDetails gd ON od.Grade = gd.Grade
)

SELECT
    final_result.Order_No,
    final_result.Order_Width,
    final_result.New_Set_Width,
    final_result.Grade,
    final_result.`Grade Grp`,
    final_result.Delivery_Date,
    final_result.L1_Group,
    final_result.Ceiling_Order_Width,
    final_result.Prev_Ceiling_Order_Width,
    final_result.Gap,
    final_result.Set_Width,
    CASE
        WHEN final_result.Set_Width IS NOT NULL THEN final_result.Set_Width + 15
        ELSE NULL
    END AS Set_Width_Plus_15,
    final_result.Diff,
    final_result.BTR_Qty,
    final_result.BTC_Qty,
    final_result.Product,
    final_result.bucket,
    final_result.total_records,
    final_result.width_rank
FROM (
    SELECT
        final_query.*,
        CASE
            WHEN final_query.Set_Width IS NOT NULL THEN final_query.Set_Width + 15 - final_query.Order_Width
            ELSE NULL
        END AS Diff,
        CASE
            WHEN final_query.Set_Width IS NULL AND 
                 (final_query.Set_Width + 15 - final_query.Order_Width) IS NULL THEN final_query.Ceiling_Order_Width
            ELSE final_query.Set_Width
        END AS New_Set_Width
    FROM (
        SELECT
            subquery.Order_No,
            subquery.Order_Width,
            subquery.Ceiling_Order_Width,
            gd.`Grade Grp`,
            subquery.Grade,
            subquery.L1_Group,
            LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date) AS Prev_Ceiling_Order_Width,
            ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) AS Gap,
            CASE
                WHEN ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) = 0 OR
                     ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) = 25 THEN NULL
                WHEN ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) = 50 THEN subquery.Ceiling_Order_Width + 25
                ELSE NULL
            END AS Set_Width,
            subquery.Delivery_Date,
            subquery.BTR_Qty,
            subquery.BTC_Qty,
            subquery.Product,
            subquery.bucket,
            subquery.total_records,
            subquery.width_rank
        FROM subquery
        LEFT JOIN GradeDetails gd ON subquery.Grade = gd.Grade
    ) AS final_query
) AS final_result
ORDER BY CAST(SUBSTRING(final_result.L1_Group, 4) AS UNSIGNED), final_result.width_rank, final_result.Delivery_Date;


-- BASE QUERY TILL L1_Group