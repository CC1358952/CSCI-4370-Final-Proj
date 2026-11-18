-- Create the database.
create database if not exists csx370_sleigh_platform;

-- Use the created database.
use csx370_sleigh_platform;

-- Create the user table.
create table if not exists user (
    userId int auto_increment,
    username varchar(30) not null,
    password varchar(255) not null,
    firstName varchar(30) not null,
    lastName varchar(30) not null,
    budget decimal(7, 2) default 0.00,
    primary key (userId),
    unique (username),
    constraint username_min_length check (char_length(trim(username)) >= 2),
    constraint first_name_min_length check (char_length(trim(firstName)) >= 2),
    constraint last_name_min_length check (char_length(trim(lastName)) >= 2)
);

create table if not exists group (
    groupId int auto_increment,
    groupName varchar(30) not null,
    password varchar(255) not null,
    exchangeDate date not null,
    primary key (groupId),
    unique (groupName),
    constraint group_name_min_length check (char_length(trim(groupName)) >= 2)
);

create table if not exists member (
    memberId int auto_increment,
    userId int not null,
    groupId int not null,
    primary key (memberId),
    unique (userId, groupId),
    foreign key (userId) references user(userId) on delete cascade,
    foreign key (groupId) references group(groupId) on delete cascade
);

create table if not exists wishlist (
    wishlistId int auto_increment,
    userId int not null,
    groupId int, -- wishlist can be created before group is created, so groupId can be null
    title varchar(30) not null,
    primary key (wishlistId),
    foreign key (userId) references user(userId) on delete cascade,
    foreign key (groupId) references group(groupId) on delete cascade
);

create table if not exists category (
    categoryId int auto_increment,
    name varchar(60) not null,
    primary key (categoryId),
    unique (name),
    constraint category_name_min_length check (char_length(trim(name)) >= 2)
);

create table if not exists product (
    productId int auto_increment,
    name varchar(30) not null,
    imgUrl varchar(255) not null,
    productUrl varchar(255) not null,
    stars decimal(2, 1) not null,
    price decimal(6, 2) not null,
    categoryId int not null,
    primary key (productId),
    foreign key (categoryId) references category(categoryId) on delete cascade,
    constraint product_name_min_length check (char_length(trim(name)) >= 2),
    constraint product_price_positive check (price >= 0),
    constraint product_stars_range check (stars >= 0 and stars <= 5)
);

create table if not exists item (
    itemId int auto_increment,
    wishlistId int not null,
    productId int not null,
    primary key (itemId),
    foreign key (wishlistId) references wishlist(wishlistId) on delete cascade,
    foreign key (productId) references product(productId) on delete cascade
);

create table if not exists gift (
    giftId int auto_increment,
    itemId int not null,
    giverId int not null,
    bought boolean default false,
    primary key (giftId),
    foreign key (itemId) references item(itemId) on delete cascade,
    foreign key (giverId) references user(userId) on delete cascade,
);