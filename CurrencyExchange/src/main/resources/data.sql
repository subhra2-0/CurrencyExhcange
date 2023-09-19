--INSERT INTO customer  VALUES ('Subhrajit Barik','subhrajit','MALE','subhra@gmail.com',1234567890,'2023-08-26 19:14:02.525339','SUPERUSER');

DELETE from customer where email = 'admin@maveric-systems.com';

INSERT INTO customer (customer_name, password, gender, email, contact_number, onboard_date, role)
VALUES ('Super User', '$2a$12$6KBYYRifWHbSlRYhCuYtv.eUtDsja4aL/LdYRLnKGZyZLkHE5Jxb.', 'MALE', 'admin@maveric-systems.com', 7008451197,CURRENT_TIMESTAMP, 'SUPERUSER');