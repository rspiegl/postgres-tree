CREATE TABLE IF NOT EXISTS edge
(
    fromId
    INT
    NOT
    NULL,
    toId
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    fromId,
    toId
)
    );
