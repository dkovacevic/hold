CREATE TABLE Access (
 userId UUID PRIMARY KEY,
 clientId varchar NOT NULL,
 token varchar,
 cookie varchar NOT NULL,
 last UUID,
 updated TIMESTAMP NOT NULL,
 created TIMESTAMP NOT NULL,
 enabled INTEGER DEFAULT 1
);

CREATE TABLE Events (
 messageId UUID PRIMARY KEY,
 conversationId UUID NOT NULL,
 type VARCHAR NOT NULL,
 payload VARCHAR NOT NULL,
 time TIMESTAMP NOT NULL
);

