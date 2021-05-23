use DBMS_hw
go

delete from astronaut

insert into astronaut (id, name, age) values (1, 'Neil Armstrong', '1930-2012'), (2, 'Buzz Aldrin', '1930-') 
select * from astronaut

-- first query
begin tran
	update astronaut set age='???' where id=1
	waitfor delay '00:00:10'
	update astronaut set age='???' where id=2
	exec log_procedure 'deadlock update', null, null 
commit tran


select * from astronaut