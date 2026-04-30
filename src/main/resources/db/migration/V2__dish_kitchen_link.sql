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

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conname = 'fk_dishes_kitchen'
  ) THEN
    ALTER TABLE dishes
    ADD CONSTRAINT fk_dishes_kitchen
    FOREIGN KEY (kitchen_id) REFERENCES kitchens(id);
  END IF;
END $$;