use DBMS_hw
go

set transaction isolation level serializable
--set transaction isolation level repeatable read

-- second query
begin tran
	select * from astronaut
	exec log_procedure 'phantom reads select', null, null
	waitfor delay '00:00:05'
	select * from astronaut
commit tran

select * from astronaut