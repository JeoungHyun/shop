create table sale(
	saleid int primary key,
	userid varchar(10) not null,
	saledate datetime
);
create table saleitem(
	saleid int ,
	seq int ,
	itemid int not null,
	quantity int,
	primary key (saleid,seq)
);
