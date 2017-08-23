create database if not exists pricetrend;

use pricetrend;

drop table if exists my_products;
create table if not exists my_products(
 u_id        INT NOT NULL,
 p_id        INT NOT NULL,
 p_name      VARCHAR(200),
 p_desc      VARCHAR(500),
 p_website   VARCHAR(100),
 p_price     FLOAT,
 p_url       VARCHAR(500)
 );

 
drop table if exists products_links;
create table if not exists products_links(
 p_id                  INT NOT NULL,         
 p_website             VARCHAR(100), 
 p_price               FLOAT,
 p_url                 VARCHAR(500),
 hashed_p_url          VARCHAR(64),
 updated_timestamp     TIMESTAMP
 );

insert into products_links values(1,'amazon',50000,'http://www.amazon.in/ADATA-512GB-Ultimate-Internal-ASU900SS-512GM-C/dp/B01NGZVN1R',null,null);
insert into products_links values(2,'flipkart',50000,'https://www.flipkart.com/samsung-850-pro-256-gb-desktop-laptop-internal-solid-state-drive-mz-7ke256bw/p/itmeyfbwnjarzvam',null,null);
insert into products_links values(2,'amazon',50000,'http://www.amazon.in/Samsung-256GB-2-5-Inch-Internal-MZ-7KE256BW/dp/B00LMXBOP4',null,null);
insert into products_links values(3,'amazon',50000,'http://www.amazon.in/F550X-2-1-Multimedia-Bluetooth-Speakers/dp/B0154M6J2Q',null,null);
insert into products_links values(3,'flipkart',50000,'https://www.flipkart.com/f-d-f550x-bluetooth-home-audio-speaker/p/itmea2aspwcaxuaz',null,null);
insert into products_links values(3,'paytmmall',50000,'https://paytmmall.com/f-d-f550x-bluetooth-speaker-black-CMPLXCOMF-D-F550X-BLDUMM1416C18D283-pdp',null,null);


drop table if exists pricetrend.all_products;
create table if not exists all_products(
 p_id            INT NOT NULL AUTO_INCREMENT,         
 p_name          VARCHAR(200),
 p_desc          VARCHAR(500),
 p_section       VARCHAR(200),
 p_category      VARCHAR(200),
 p_sub_category  VARCHAR(200),
 p_type          VARCHAR(200),
 p_spec_1        VARCHAR(200),
 p_spec_2        VARCHAR(200),
 p_spec_3        VARCHAR(200),
 p_spec_4        VARCHAR(200),
 p_spec_5        VARCHAR(200),
 PRIMARY KEY (p_id)
);

insert into all_products values (1,'adata su900 ssd','adata','electronics','storage','ssd','512gb',null,null,null,null);
insert into all_products values (2,'samsung 850 pro ssd','samsung','electronics','storage','ssd','256gb',null,null,null,null);
insert into all_products values (3,'f&d f550x speakers','fnd audio','electronics','speakers','bluetooth speakers','2.1 surround',null,null,null,null); 



drop table if exists pricetrend.crawled_data;
create table if not exists crawled_data(
domain           VARCHAR(200),
anchor           VARCHAR(200),
crawl_timestamp  TIMESTAMP,
p_url            VARCHAR(500),
non_p_url        VARCHAR(500),
hashed_url       VARCHAR(64)
);


