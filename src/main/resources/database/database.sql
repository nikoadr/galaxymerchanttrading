--create db
CREATE DATABASE galaxymerchant;

-- create roman_numeric_config table
CREATE TABLE public.roman_numeric_config (
    num_code character varying(1) NOT NULL,
    num_value integer
);

ALTER TABLE public.roman_numeric_config OWNER TO postgres;
ALTER TABLE ONLY public.roman_numeric_config
    ADD CONSTRAINT pk PRIMARY KEY (num_code);

-- insert data to roman_numeri_config table
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('I', 1);
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('X', 10);
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('V', 5);
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('L', 50);
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('C', 100);
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('D', 500);
INSERT INTO public.roman_numeric_config (num_code, num_value) VALUES ('M', 1000);


-- create intergalactic_unit_config table
CREATE TABLE public.intergalactic_unit_config (
    intergalactic_unit_name character varying(50),
    roman_numeral character varying(1)
);

ALTER TABLE public.intergalactic_unit_config OWNER TO postgres;


-- create comodity table
CREATE TABLE public.comodity (
    comodity_name character varying(100),
    comodity_value double precision
);

ALTER TABLE public.comodity OWNER TO postgres;





