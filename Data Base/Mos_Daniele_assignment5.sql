use master;
go

create table dbo.Ta (
	aid int primary key identity(1, 1),
	a2 int unique,
	a3 int
)

create table dbo.Tb (
	bid int primary key identity(1, 1),
	b2 int,
	b3 int
)

create table dbo.Tc (
	cid int primary key identity(1, 1),
	aid int references Ta(aid),
	bid int references Tb(bid)
)


insert into Ta(a2, a3) values (1, 1)
insert into Ta(a2, a3) values (2, 2)
insert into Ta(a2, a3) values (3, 3)
insert into Ta(a2, a3) values (4, 4)
insert into Ta(a2, a3) values (5, 5)
insert into Ta(a2, a3) values (6, 6)

insert into Tb(b2, b3) values (1, 1)
insert into Tb(b2, b3) values (2, 2)
insert into Tb(b2, b3) values (3, 3)
insert into Tb(b2, b3) values (4, 4)
insert into Tb(b2, b3) values (5, 5)
insert into Tb(b2, b3) values (6, 6)

insert into Tc(aid, bid) values (1, 1)
insert into Tc(aid, bid) values (2, 2)
insert into Tc(aid, bid) values (3, 3)
insert into Tc(aid, bid) values (4, 4)
insert into Tc(aid, bid) values (5, 5)
insert into Tc(aid, bid) values (6, 6)

-- a) clustered index scan
select * from Ta

-- a) clustered index seek
select aid from Ta where aid > 5

-- a) nonclustered index scan
select a2 from Ta

-- a) nonclustered index seek
select a2 from Ta where a2 > 1

-- a) key lookup
select aid from Ta

-- b) 
--Write a query on table Tb with a WHERE clause of the form WHERE b2 = value and analyze its execution plan. 
--Create a nonclustered index that can speed up the query. Examine the execution plan again.
select b2 from Tb where b2 = 5
if exists(select * from sys.indexes where name = 'index_b2')
	drop index index_b2 on Tb
create nonclustered index index_b2 on Tb(b2)
select b2 from Tb where b2 = 5

go
-- c)
-- Create a view that joins at least 2 tables. Check whether existing indexes are helpful; 
-- if not, reassess existing indexes / examine the cardinality of the tables.
create view view_t as
	select Tc.cid, Tc.aid
	from Tc inner join Ta T on T.aid = Tc.aid
	where Tc.aid between 1 and 100

go
--clustered index scan on Tc and clustered index seek on Ta
select * from view_t
if exists(select * from sys.indexes where name = 'index_aid')
	drop index index_aid on Tc
create nonclustered index index_aid on Tc(aid)

select * from view_t