# --- !Ups
CREATE SEQUENCE RESOURCE_SEQ;

CREATE TABLE "RESOURCE" (
    "ID" text NOT NULL DEFAULT 'RES'||nextval('RESOURCE_SEQ'::regclass)::TEXT,
    "TYPE" text,
    "NAME" text,
    "DESCRIPTION" text,
    "USER_DEFINED_PROPERTIES" jsonb,
    PRIMARY KEY ("ID")
    --TODO: add time stamp for data create/update
    --TODO: flag for record status [eg: delete resource]
);




# --- !Downs
DROP TABLE IF EXISTS RESOURCE CASCADE;
DROP SEQUENCE IF EXISTS RESOURCE_SEQ;