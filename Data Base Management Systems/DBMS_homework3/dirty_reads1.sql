use DBMS_hw
go

delete from astronaut

insert into astronaut (id, name, age) values (1, 'Neil Armstrong', '1930-2012'),(2, 'Buzz Aldrin', '1930-') 
select * from astronaut

-- first query
begin tran
	declare @name varchar(50)
	update astronaut set name='NeilArmstrong', @name=name, age='82' where id=1
	exec log_procedure 'dirty reads update', @name, null 
	waitfor delay '00:00:10'
rollback tran
