ALTER TABLE public.users
    DROP CONSTRAINT users_user_type_check;

ALTER TABLE public.users
    ADD CONSTRAINT users_user_type_check CHECK (role::text = ANY (ARRAY['Admin'::character varying, 'Student'::character varying, 'Teacher'::character varying, 'Pending'::character varying]::text[]));
