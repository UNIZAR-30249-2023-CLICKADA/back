PGDMP         8                {           edificio-bd    11.18    15.2 	    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16497    edificio-bd    DATABASE     �   CREATE DATABASE "edificio-bd" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "edificio-bd";
             
   clickadabd    false            �           0    0    edificio-bd    DATABASE PROPERTIES     O   ALTER DATABASE "edificio-bd" SET search_path TO '$user', 'public', 'topology';
                  
   clickadabd    false            �            1259    16503    persona    TABLE       CREATE TABLE public.persona (
    id_persona uuid NOT NULL,
    contrasenya character varying(255),
    departamento_disponible boolean NOT NULL,
    e_mail character varying(255),
    nombre character varying(255),
    roles bytea,
    adscripcion bytea,
    departamento integer
);
    DROP TABLE public.persona;
       public         
   clickadabd    false            �          0    16503    persona 
   TABLE DATA           �   COPY public.persona (id_persona, contrasenya, departamento_disponible, e_mail, nombre, roles, adscripcion, departamento) FROM stdin;
    public       
   clickadabd    false    204   �	                   2606    16510    persona persona_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.persona
    ADD CONSTRAINT persona_pkey PRIMARY KEY (id_persona);
 >   ALTER TABLE ONLY public.persona DROP CONSTRAINT persona_pkey;
       public         
   clickadabd    false    204            "           2606    16512 $   persona uk_lkr38tx1gvyo0x4s8ykb2aey1 
   CONSTRAINT     a   ALTER TABLE ONLY public.persona
    ADD CONSTRAINT uk_lkr38tx1gvyo0x4s8ykb2aey1 UNIQUE (e_mail);
 N   ALTER TABLE ONLY public.persona DROP CONSTRAINT uk_lkr38tx1gvyo0x4s8ykb2aey1;
       public         
   clickadabd    false    204            �   �  x����n�0 г���p���M�ڣ/\���b�(�W���X�^�,P9JI��F�1S�Х�H'�rBT�EF�\�����ðn�iyY�r�j�(���D���w*\��֣7Z����;����+�	��F⑊�r'���1� V��P�=y�{��>x}��{M���{2��5WwGiU�ف�޳��zYΨ�#��XJ�q���2�@�6ȘPzD��H��_l`�^Fԏ|�c���y�coۺ�fM���"�ud-Z�sO@D����5��@���>��AO@���A���T�V�#��eg�Ѫ�
/7������� 6�ۈ[{Kpo�޲6M�l��h�
��L�Xe:Zm�,�e����ζ�"V�X\$�Ī���:����xʪ��s���&P��WQ������<N��j�7�:O������i�EVh���י]O��S�J����+M)�hE��#OY-�|��iΦ	��H^|�4�	Kd<VR�|- ����!7��:�����k�!8���"]�8�L���B�1�`�Y��6dR�5b�$����.���C�����=�my1��Z�7���/�JAs�o����2xBs�W���������2�4EWe�i���E�nĈ:�����1"=���������!ݎ0����k�
bY~{\U����\C8��j��|����[I�,W��z:�����b2�����     