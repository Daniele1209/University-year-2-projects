import torch
import torch.nn.functional as F
import numpy as np

import myModel

device = torch.device("cuda:0")

filepath = "myNetwork.pt"
ann = myModel.Net(2, 10, 1).cuda()

ann.load_state_dict(torch.load(filepath))
ann.eval()

x1 = float(input("x1 = "))
x2 = float(input("x2 = "))
x = torch.tensor([x1, x2]).to(device)
print(ann(x).tolist())
print(f'correct: {np.sin(x1 + x2 / np.pi)}')