use DBMS_hw
go

delete from astronaut

insert into astronaut (id, name, age) values (1, 'Neil Armstrong', '1930-2012')
select * from astronaut

-- first query
begin tran
	waitfor delay '00:00:05'
	insert into astronaut (id, name, age) values (2, 'Buzz Aldrin', '1930-') 
	exec log_procedure 'phantom reads insert', 'Buzz Aldrin', null 
commit tran
