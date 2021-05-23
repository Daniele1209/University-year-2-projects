use DBMS_hw
go

set transaction isolation level read committed
--set transaction isolation level read uncommitted

-- second query
begin tran
	select * from astronaut
	exec log_procedure 'dirty reads select', null, null
	waitfor delay '00:00:10'
	select * from astronaut
commit tran

