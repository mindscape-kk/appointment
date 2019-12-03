# --- !Ups
CREATE SEQUENCE RESOURCE_SEQ;
CREATE SEQUENCE TIMESLOT_SEQ;
CREATE SEQUENCE TIMESLOT_PATTERN_SEQ;

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

CREATE TABLE "TIMESLOT" (
    "ID" text NOT NULL DEFAULT 'TS'||nextval('TIMESLOT_SEQ'::regclass)::TEXT,
    "RESOURCE_ID" text NOT NULL,
    "BEGIN" timestamp,
    "DURATION" integer,
    "ASSIGNEE" text,
    "CONFIRMED" boolean,
    "CANCEL_REQUEST" boolean,
    PRIMARY KEY ("ID")
    --TODO: add time stamp for data create/update
);

CREATE TABLE "TIMESLOT_PATTERN" (
    "ID" text NOT NULL DEFAULT 'TSP'||nextval('TIMESLOT_PATTERN_SEQ'::regclass)::TEXT,
    "RESOURCE_ID"  text NOT NULL,
    "START_DATE" date,
    "END_DATE" date,
    "START_TIME" timestamp,
    "DURATION" integer,
    "REPEAT_PER_DAY" integer,
    "REPEAT_ON_DAYS" text,
    PRIMARY KEY ("ID")
    --TODO: add time stamp for data create/update
);




# --- !Downs
DROP TABLE IF EXISTS RESOURCE CASCADE;
DROP TABLE IF EXISTS TIMESLOT CASCADE;
DROP TABLE IF EXISTS TIMESLOT_PATTERN CASCADE;
DROP SEQUENCE IF EXISTS RESOURCE_SEQ;
DROP SEQUENCE IF EXISTS TIMESLOT_SEQ;
DROP SEQUENCE IF EXISTS TIMESLOT_PATTERN_SEQ;