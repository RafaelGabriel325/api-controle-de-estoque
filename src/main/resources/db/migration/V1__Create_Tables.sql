CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS public.tab_permission (
    id uuid default uuid_generate_v4() NOT NULL,
    description varchar(180) NOT NULL,
    CONSTRAINT tab_permission_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.tab_pessoa (
    id uuid default uuid_generate_v4() NOT NULL,
    nome varchar(25) NOT NULL,
    sobrenome varchar(100) NOT NULL,
    CONSTRAINT tab_pessoa_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.tab_tipo_produto (
    id uuid default uuid_generate_v4() NOT NULL,
    nome varchar(6) NOT NULL,
    CONSTRAINT tab_tipo_produto_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.tab_user (
    id uuid default uuid_generate_v4() NOT NULL,
    account_non_expired bool NULL,
    account_non_locked bool NULL,
    credentials_non_expired bool NULL,
    "enable" bool NULL,
    user_name varchar(20) NOT NULL,
    full_name varchar(100) NOT NULL,
    "password" varchar(100) NOT NULL,
    CONSTRAINT tab_user_pkey PRIMARY KEY (id),
    CONSTRAINT tab_user_user_name_key UNIQUE (user_name)
    );

CREATE TABLE IF NOT EXISTS public.tab_produto_estoque (
    id uuid default uuid_generate_v4() NOT NULL,
    data_entrega date NOT NULL,
    quantidade_pacote int4 NOT NULL,
    tamanho_pacote varchar(5) NOT NULL,
    pessoa_id uuid NOT NULL,
    tipo_pessoa_id uuid NOT NULL,
    marca varchar(25) NOT NULL,
    CONSTRAINT tab_produto_estoque_pkey PRIMARY KEY (id),
    CONSTRAINT fkk0eunfoua8qxfyj6slh0uh1tm FOREIGN KEY (pessoa_id) REFERENCES public.tab_pessoa(id),
    CONSTRAINT fkoym33bx8fspsu7skr9ebdh41v FOREIGN KEY (tipo_pessoa_id) REFERENCES public.tab_tipo_produto(id)
    );

CREATE TABLE IF NOT EXISTS public.tab_user_permission (
   permission_id uuid NOT NULL,
   user_id uuid NOT NULL,
   CONSTRAINT fkjap735sl0veppax04wnkytuad FOREIGN KEY (user_id) REFERENCES public.tab_user(id),
   CONSTRAINT fks9btpyuvbr3ppteplbgroxdig FOREIGN KEY (permission_id) REFERENCES public.tab_permission(id)
    );