INSERT INTO public.tab_permission (description)
VALUES ('ADMIN'), ('COMUM');

INSERT INTO public.tab_tipo_produto (nome)
VALUES ('Café'), ('Açúcar');

INSERT INTO public.tab_pessoa (nome, sobrenome)
VALUES ('Rafael', 'Gabriel'), ('José', 'Walter');

INSERT INTO public.tab_user (user_name, full_name, password, account_non_expired, account_non_locked, credentials_non_expired, enable)
VALUES ('rafael325', 'Rafael Gabriel Ferreira da Silva', '$2a$10$pY6TqrznX2elyclubdW8r.WiE3PR/nyMl6BnlJxzi5V.lLWYseb7y', true, true, true, true);

INSERT INTO public.tab_user_permission (user_id, permission_id)
VALUES (
           (SELECT id FROM public.tab_user WHERE user_name = 'rafael325'),
           (SELECT id FROM public.tab_permission WHERE description = 'ADMIN')
       );