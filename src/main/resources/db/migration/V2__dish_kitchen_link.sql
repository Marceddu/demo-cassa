ALTER TABLE dishes ADD COLUMN IF NOT EXISTS kitchen_id BIGINT;

UPDATE dishes d
SET kitchen_id = (SELECT id FROM kitchens WHERE name = 'Panini')
WHERE kitchen_id IS NULL AND (UPPER(d.name) LIKE '%PRUPPEDDA%' OR UPPER(d.name) LIKE '%VRATTAU%');

UPDATE dishes d
SET kitchen_id = (SELECT id FROM kitchens WHERE name = 'Cucina Dentro')
WHERE kitchen_id IS NULL AND UPPER(d.name) LIKE '%COMPLETO%';

UPDATE dishes d
SET kitchen_id = (SELECT id FROM kitchens WHERE name = 'Panini')
WHERE kitchen_id IS NULL;

ALTER TABLE dishes ADD CONSTRAINT IF NOT EXISTS fk_dishes_kitchen
FOREIGN KEY (kitchen_id) REFERENCES kitchens(id);
