-- Table: public.customers
CREATE TABLE public.customers (
    _id serial PRIMARY KEY,
    user_name character varying(255),
    email_id character varying(255),
    phone_number integer,
    created timestamp(0) without time zone,
    modified timestamp(0) without time zone
);

-- Table: public.food_items
CREATE TABLE public.food_items (
    _id serial PRIMARY KEY,
    name character varying(255),
    quantity integer,
    price numeric(10,2),
    created timestamp(0) without time zone,
    modified timestamp(0) without time zone
);

-- Table: public.food_menu
CREATE TABLE public.food_menu (
    _id serial PRIMARY KEY,
    name character varying(255),
    available_day character varying(255),
    created timestamp(0) without time zone,
    modified timestamp(0) without time zone
);

-- Table: public.orders
CREATE TABLE public.orders (
    _id serial PRIMARY KEY,
    customer_id integer,
    total_cost numeric,
    order_status character varying(255),
    created timestamp(0) without time zone,
    FOREIGN KEY (customer_id) REFERENCES public.customers(_id)
);

-- Table: public.delivery
CREATE TABLE public.delivery (
    _id serial PRIMARY KEY,
    order_id integer,
    delivery_location character varying(255),
    delivery_date_time timestamp(0) without time zone,
    FOREIGN KEY (order_id) REFERENCES public.orders(_id)
);

-- Table: public.food_menu_items_map
CREATE TABLE public.food_menu_items_map (
    _id serial PRIMARY KEY,
    food_menu_id integer NOT NULL,
    food_item_id integer NOT NULL,
    FOREIGN KEY (food_menu_id) REFERENCES public.food_menu(_id),
    FOREIGN KEY (food_item_id) REFERENCES public.food_items(_id)
);

-- Table: public.order_food_items_map
CREATE TABLE public.order_food_items_map (
    _id serial PRIMARY KEY,
    order_id integer,
    food_item_name character varying(255),
    quantity integer,
    FOREIGN KEY (order_id) REFERENCES public.orders(_id)
);