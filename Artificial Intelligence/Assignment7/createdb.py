import numpy as np
import torch
import random


def generateDB():
    point_count = 1000
    lower_limit = -10
    upper_limit = 10

    x1 = torch.rand(point_count) * (upper_limit - lower_limit) + lower_limit
    x2 = torch.rand(point_count) * (upper_limit - lower_limit) + lower_limit

    f_tensor = torch.sin(x1 + (x2 / np.pi))
    data = torch.stack((x1, x2, f_tensor))
    torch.save(data, "mydataset.dat")


generateDB()
