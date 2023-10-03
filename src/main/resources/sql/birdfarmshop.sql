DROP DATABASE IF EXISTS birdfarmshop;
CREATE DATABASE birdfarmshop;
USE birdfarmshop;

CREATE TABLE roles(
    id INT PRIMARY KEY AUTO_INCREMENT ,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL ,
    email VARCHAR(150) ,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) DEFAULT '',
    image_url VARCHAR(200),
    role_id INT NOT NULL ,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    provider_id INT DEFAULT 0 ,
    provider enum("local","google","facebook") ,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE tokens(
    id int PRIMARY KEY AUTO_INCREMENT,
    token varchar(255) UNIQUE NOT NULL,
    expiration_date DATETIME,
    revoked tinyint(1) NOT NULL,
    expired tinyint(1) NOT NULL,
    user_id int,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) NOT NULL DEFAULT ''
);

CREATE TABLE bird_types(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) NOT NULL DEFAULT ''
);

CREATE TABLE birds(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) ,
    price FLOAT CHECK (price >= 0),
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT ,
    gender ENUM('male','female'),
    purebred_level VARCHAR(10),
    competition_achievements INT DEFAULT 0,
    age VARCHAR(100),
    quantity INT  DEFAULT 1,
    color VARCHAR(50),
    type_id INT NOT NULL,
    category_id INT NOT NULL,
    status TINYINT(1) DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (type_id) REFERENCES bird_types (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)

);

CREATE TABLE bird_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    bird_id INT,
    FOREIGN KEY (bird_id) REFERENCES birds (id),
    CONSTRAINT fk_product_images_bird_id
        FOREIGN KEY (bird_id)
        REFERENCES birds (id) ON DELETE CASCADE,
    image_url VARCHAR(300)
);

CREATE TABLE booking(
    id INT PRIMARY KEY AUTO_INCREMENT,
    bird_pairing_id INT,
    user_id INT NOT NULL,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    shipping_address VARCHAR(200),
    shipping_method VARCHAR(100),
    payment_method VARCHAR(100),
    manager_id INT NOT NULL,
    status ENUM ('pending', 'confirmed', 'completed', 'cancelled'),
    payment_deposit FLOAT CHECK (payment_deposit >= 0),
    total_payment FLOAT CHECK (total_payment >= 0),
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (manager_id) REFERENCES users (id)

);

CREATE TABLE booking_detail(
    id  INT AUTO_INCREMENT ,
    booking_id INT ,
    bird_types_id INT,
    father_bird_id INT,
    mother_bird_id INT,
    status ENUM ('breeding_in_progress', 'brooding', 'fledgling'),
    created_at DATETIME,
    updated_at DATETIME,
    Constraint PK_bird_pairing Primary Key (id, booking_id),
    FOREIGN KEY (bird_types_id) REFERENCES bird_types (id),
    FOREIGN KEY (booking_id) REFERENCES booking (id),
    FOREIGN KEY (father_bird_id) REFERENCES birds (id),
    FOREIGN KEY (mother_bird_id) REFERENCES birds (id)
);

CREATE TABLE bird_pairing(
    new_bird_id INT,
    booking_detail_id INT,
    status ENUM ('received','not_received'),
    created_at DATETIME,
    updated_at DATETIME,
    Constraint PK_bird_pairing Primary Key (new_bird_id, booking_detail_id),
    FOREIGN KEY (new_bird_id) REFERENCES birds (id),
    FOREIGN KEY (booking_detail_id) REFERENCES booking_detail (id)

);


 CREATE TABLE vouchers(
    id INT PRIMARY KEY AUTO_INCREMENT,
    discount INT,
    name VARCHAR(100),
    amount INT,
    description VARCHAR (200),
    expiration_date DATETIME,
    status TINYINT(1)
 );

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'processing', 'shipping', 'delivered', 'cancelled'),
    total_money FLOAT CHECK(total_money >= 0),
    shipping_method VARCHAR(100),
    shipping_address VARCHAR(200),
    shipping_date DATE,
    tracking_number VARCHAR(100),
    payment_method VARCHAR(100),
    voucher_id INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (voucher_id) REFERENCES vouchers (id)
);




CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    bird_id INT,
    FOREIGN KEY (bird_id) REFERENCES birds (id),
    number_of_products INT CHECK(number_of_products > 0)


);
CREATE TABLE feedback(
    id INT PRIMARY KEY AUTO_INCREMENT,
    comment VARCHAR(350),
    rating INT,
    order_id INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

ALTER TABLE booking
ADD booking_time DATETIME;

