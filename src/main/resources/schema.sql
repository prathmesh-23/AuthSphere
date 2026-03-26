-- schema.sql
-- Complete RBAC schema for Spring Boot + PostgreSQL

-- =========================
-- Roles Table
-- =========================
CREATE TABLE IF NOT EXISTS public.roles (
role_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1START 1  MINVALUE 1 MAXVALUE 9223372036854775807CACHE 1
),
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT roles_pkey PRIMARY KEY (role_id),
    CONSTRAINT roles_role_name_key UNIQUE (role_name)
    );

-- Seed roles
INSERT INTO public.roles (role_name, description)
VALUES
    ('USER', 'Default role for standard users'),
    ('ADMIN', 'Administrative role with elevated privileges')
    ON CONFLICT (role_name) DO NOTHING;

-- =========================
-- Users Table
-- =========================
CREATE SEQUENCE IF NOT EXISTS public.users_userid_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.users (
                                            userid BIGINT NOT NULL DEFAULT nextval('users_userid_seq'::regclass),
    username VARCHAR(100) NOT NULL,
    email_enc VARCHAR(100) NOT NULL,
    pass_enc VARCHAR(100) NOT NULL,
    isactive INTEGER NOT NULL,
    isdeleted INTEGER NOT NULL,
    hash_key VARCHAR(100),
    otp VARCHAR(10),
    otpexptime TIMESTAMP WITHOUT TIME ZONE,
    role_id BIGINT,
    CONSTRAINT users_pkey PRIMARY KEY (userid),
    CONSTRAINT users_email_enc_key UNIQUE (email_enc),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id)
    REFERENCES public.roles (role_id)
                         ON UPDATE NO ACTION
                         ON DELETE NO ACTION
    );

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON public.users (username);
CREATE INDEX IF NOT EXISTS idx_users_isactive ON public.users (isactive);