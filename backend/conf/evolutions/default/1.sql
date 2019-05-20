# --- !Ups
CREATE TABLE "RESOURCE" (
    "ID" text NOT NULL,
    "TYPE" text,
    "NAME" text,
    "DESCRIPTION" text,
    "USER_DEFINED_PROPERTIES" jsonb,
    PRIMARY KEY ("ID")
);



# --- !Downs
DROP TABLE IF EXISTS RESOURCE CASCADE;