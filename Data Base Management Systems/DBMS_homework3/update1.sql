use DBMS_hw
go

delete from astronaut

insert into astronaut (id, name, age) values (1, 'Neil Armstrong', '1930-2012'), (2, 'Buzz Aldrin', '1930-') 
select * from astronaut

alter database DBMS_hw set allow_snapshot_isolation on

-- first query
waitfor delay '00:00:10'
begin tran
	declare @name varchar(50)
	update astronaut set age='???', @name=name where id=1
	waitfor delay '00:00:10'
	exec log_procedure 'update conflict update', @name, null 
commit tran

alter database DBMS_hw set allow_snapshot_isolation OFF
select * from astronaut