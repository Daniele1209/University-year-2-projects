use DBMS_hw
go

create table rocket (
	id int primary key,
	model varchar(max),
	company varchar(max)
)

create table launch_date (
	id int primary key,
	date_launch date
)

create table launch (
	rid int references rocket(id),
	lid int references launch_date(id),
	primary key(rid, lid)
)

create table astronaut (
	id int primary key,
	name varchar(max),
	age varchar(max)
)

select * from rocket
select * from launch_date
select * from launch
select * from astronaut