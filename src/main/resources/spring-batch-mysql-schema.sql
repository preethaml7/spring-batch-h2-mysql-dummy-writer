-- ===============================
-- Sequence Simulation Tables for MySQL
-- ===============================

-- Sequence for tracking BATCH_STEP_EXECUTION IDs
CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_SEQ
(
    ID BIGINT NOT NULL
);

-- Initialize the BATCH_STEP_EXECUTION_SEQ sequence with a value of 0
INSERT INTO BATCH_STEP_EXECUTION_SEQ
VALUES (0);

-- Sequence for tracking BATCH_JOB_EXECUTION IDs
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_SEQ
(
    ID BIGINT NOT NULL
);

-- Initialize the BATCH_JOB_EXECUTION_SEQ sequence with a value of 0
INSERT INTO BATCH_JOB_EXECUTION_SEQ
VALUES (0);

-- Sequence for tracking BATCH_JOB IDs
CREATE TABLE IF NOT EXISTS BATCH_JOB_SEQ
(
    ID BIGINT NOT NULL
);

-- Initialize the BATCH_JOB_SEQ sequence with a value of 0
INSERT INTO BATCH_JOB_SEQ
VALUES (0);

-- =======================
-- BATCH JOB TABLES
-- =======================

-- Table to store instances of a job
CREATE TABLE IF NOT EXISTS BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT PRIMARY KEY,    -- Unique ID for the job instance
    VERSION         BIGINT,                -- Version of the job instance
    JOB_NAME        VARCHAR(100) NOT NULL, -- Name of the job
    JOB_KEY         VARCHAR(32)  NOT NULL  -- Unique key for the job instance
);

-- Table to store execution details of each job
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID BIGINT PRIMARY KEY,                -- Unique ID for the job execution
    VERSION          BIGINT,                            -- Version of the job execution
    JOB_INSTANCE_ID  BIGINT    NOT NULL,                -- Foreign key to the job instance
    CREATE_TIME      TIMESTAMP NOT NULL,                -- Time when the execution was created
    START_TIME       TIMESTAMP DEFAULT NULL,            -- Time when the execution started
    END_TIME         TIMESTAMP DEFAULT NULL,            -- Time when the execution ended
    STATUS           VARCHAR(10),                       -- Status of the job execution (e.g., STARTED, COMPLETED)
    EXIT_CODE        VARCHAR(20),                       -- Exit code of the job execution
    EXIT_MESSAGE     VARCHAR(2500),                     -- Exit message (e.g., success or error description)
    LAST_UPDATED     TIMESTAMP,                         -- Last update time
    CONSTRAINT JOB_INSTANCE_EXECUTION_FK FOREIGN KEY (JOB_INSTANCE_ID)
        REFERENCES BATCH_JOB_INSTANCE (JOB_INSTANCE_ID) -- Foreign key reference to BATCH_JOB_INSTANCE
);

-- Table to store parameters associated with each job execution
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       NOT NULL,               -- Foreign key to the job execution
    PARAMETER_NAME   VARCHAR(100) NOT NULL,               -- Name of the parameter
    PARAMETER_TYPE   VARCHAR(100) NOT NULL,               -- Type of the parameter (e.g., String, Integer)
    PARAMETER_VALUE  VARCHAR(2500),                       -- Value of the parameter
    IDENTIFYING      CHAR(1)      NOT NULL,               -- A flag to identify if the parameter is unique
    CONSTRAINT JOB_EXEC_PARAMS_FK FOREIGN KEY (JOB_EXECUTION_ID)
        REFERENCES BATCH_JOB_EXECUTION (JOB_EXECUTION_ID) -- Foreign key reference to BATCH_JOB_EXECUTION
);

-- Table to store the context information for each job execution
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT PRIMARY KEY,                -- Foreign key to the job execution
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,            -- A short description of the context
    SERIALIZED_CONTEXT TEXT,                              -- Serialized context for the execution
    CONSTRAINT JOB_EXEC_CTX_FK FOREIGN KEY (JOB_EXECUTION_ID)
        REFERENCES BATCH_JOB_EXECUTION (JOB_EXECUTION_ID) -- Foreign key reference to BATCH_JOB_EXECUTION
);

-- =======================
-- STEP EXECUTION TABLES
-- =======================

-- Table to store the execution details for each step within a job
CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT PRIMARY KEY,                -- Unique ID for the step execution
    VERSION            BIGINT       NOT NULL,             -- Version of the step execution
    STEP_NAME          VARCHAR(100) NOT NULL,             -- Name of the step
    JOB_EXECUTION_ID   BIGINT       NOT NULL,             -- Foreign key to the job execution
    CREATE_TIME        TIMESTAMP    NOT NULL,             -- Time when the step execution was created
    START_TIME         TIMESTAMP DEFAULT NULL,            -- Time when the step execution started
    END_TIME           TIMESTAMP DEFAULT NULL,            -- Time when the step execution ended
    STATUS             VARCHAR(10),                       -- Status of the step execution (e.g., STARTED, COMPLETED)
    COMMIT_COUNT       BIGINT,                            -- Number of commits made during the step
    READ_COUNT         BIGINT,                            -- Number of records read during the step
    FILTER_COUNT       BIGINT,                            -- Number of records filtered during the step
    WRITE_COUNT        BIGINT,                            -- Number of records written during the step
    READ_SKIP_COUNT    BIGINT,                            -- Number of records skipped while reading
    WRITE_SKIP_COUNT   BIGINT,                            -- Number of records skipped while writing
    PROCESS_SKIP_COUNT BIGINT,                            -- Number of records skipped while processing
    ROLLBACK_COUNT     BIGINT,                            -- Number of rollbacks during the step
    EXIT_CODE          VARCHAR(20),                       -- Exit code of the step execution
    EXIT_MESSAGE       VARCHAR(2500),                     -- Exit message (e.g., success or error description)
    LAST_UPDATED       TIMESTAMP,                         -- Last update time
    CONSTRAINT JOB_EXECUTION_STEP_FK FOREIGN KEY (JOB_EXECUTION_ID)
        REFERENCES BATCH_JOB_EXECUTION (JOB_EXECUTION_ID) -- Foreign key reference to BATCH_JOB_EXECUTION
);

-- Table to store the context information for each step execution
CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT PRIMARY KEY,                  -- Foreign key to the step execution
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,              -- A short description of the step context
    SERIALIZED_CONTEXT TEXT,                                -- Serialized context for the step execution
    CONSTRAINT STEP_EXEC_CTX_FK FOREIGN KEY (STEP_EXECUTION_ID)
        REFERENCES BATCH_STEP_EXECUTION (STEP_EXECUTION_ID) -- Foreign key reference to BATCH_STEP_EXECUTION
);
