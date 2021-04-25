from repository import *
from controller import *
from gui import *
from ui import *

if __name__ == "__main__":
    repository = repository()
    controller = controller(repository)
    ui = ui(controller, repository)

    ui.run()
