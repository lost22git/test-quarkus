
alter schema "test-quarkus" owner to lost;

CREATE EXTENSION IF NOT EXISTS btree_gist SCHEMA "test-quarkus";
