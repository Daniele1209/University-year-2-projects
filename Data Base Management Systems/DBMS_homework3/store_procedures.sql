use DBMS_hw
go

create or alter procedure AddRocket (@model varchar(50), @company varchar(50)) as
    declare @maxId int
	set @maxId = 0
	select top 1 @maxId = id + 1 from rocket order by id desc
	if (@model is null)
	begin
		raiserror('Rocket model is null !', 24, 1);
	end
	if (@company is null)
	begin
		raiserror('Rocket company is null !', 24, 1);
	end
	insert into rocket (id, model, company) values (@maxId, @model, @company)
    exec log_procedure 'insert rocket', @model, null
go

create or alter procedure AddLaunchDate (@date date) as
    declare @maxId int
	set @maxId = 0
	select top 1 @maxId = id + 1 from launch_date order by id desc
	if (@date is null)
	begin
		raiserror('Launch date is null !', 24, 1);
	end
	insert into launch_date (id, date_launch) values (@maxId, @date)
    exec log_procedure 'insert launch date', @date, null
go

create OR alter procedure AddLaunch (@model varchar(50), @date_l date) as
	if (@model is null)
	begin
		raiserror('Rocket model can not be null', 24, 1);
	end

	if (@date_l is null)
	begin
		raiserror('Launch date can not be null', 24, 1);
	end

	declare @rocket_id INT
	declare @date_id INT

	set @rocket_id = (select id from rocket where model = @model)
	set @date_id = (select id from launch_date where date_launch = @date_l)

	if (@rocket_id is null)
	begin
		raiserror('Rocket not found !', 24, 1);
	end
	if (@date_id is null)
	begin
		raiserror('Launch date not found !', 24, 1);
	end

	insert into launch (rid, lid) values (@rocket_id, @date_id)

	exec log_procedure 'insert launch', @rocket_id, null
go

create or alter procedure SuccessRollback as
	begin tran
	begin try
		exec AddRocket 'Agena-D ', 'NASA'
		exec AddLaunchDate '1970-02-04'
		exec AddLaunch 'Agena-D', '1970-02-04'
	end try
	begin catch
		rollback tran
		return
	end catch
	commit tran
go

create or alter procedure FailRollback as
	begin tran
	begin try
		exec AddRocket 'Agena-D ', 'NASA'
		exec AddLaunchDate '1970-02-04'
		exec AddLaunch 'Agena-D', '1900-06-09'
	end try
	begin catch
		rollback tran
		return
	end catch
	commit tran
GO

create or alter procedure SuccessCommit as
	declare @errors int
	set @errors = 0

	begin try
		exec AddRocket 'Falcon 1', 'SpaceX'
	end try
	begin catch
		set @errors = @errors + 1
	end catch

	begin try
		exec AddLaunchDate '2008-09-28'
	end try
	begin catch
		SET @errors = @errors + 1
	end catch

	if (@errors = 0) begin
		begin try
			exec AddLaunch 'Falcon 1', '2008-09-28'
		end try
		begin catch
		end catch
	end
go

create or alter procedure FailCommit as
	declare @errors int
	set @errors = 0
	begin try
		exec AddRocket 'Falcon 9', 'SpaceX'
	end try
	begin catch
		set @errors = 1
	end catch

	begin try
		exec AddLaunchDate '2015-12-22'
	end try
	begin catch
		set @errors = 1
	end catch

	if (@errors = 0) begin
		begin try
			exec AddLaunch 'Dragon', '2015-12-22'
		end try
		begin catch
		end catch
	end
go


DELETE FROM rocket
DELETE FROM launch_date
DELETE FROM launch
DELETE FROM LogTable

exec SuccessRollback
exec FailRollback
exec SuccessCommit
exec FailCommit

select * from rocket
select * from launch_date
select * from launch
select * from LogTable