import torch
import torch.nn.functional as F
import torch

device = torch.device("cuda:0")
class Net(torch.nn.Module):
    # the class for the network

    def __init__(self, n_feature, n_hidden, n_output):
        # we have 3 layers: 2 hidden and an output one
        super(Net, self).__init__()
        self.hidden = torch.nn.Linear(n_feature, n_hidden).to(device)
        self.hidden2 = torch.nn.Linear(n_hidden, n_hidden).to(device)
        self.hidden3 = torch.nn.Linear(n_hidden, n_hidden).to(device)
        self.output = torch.nn.Linear(n_hidden, n_output).to(device)

    def forward(self, x):
        # a function that implements the forward propagation of the signal
        # observe the refu function applied on the output of the hidden layer
        x = F.relu(self.hidden(x))
        x = F.relu(self.hidden2(x))
        x = F.relu(self.hidden3(x))
        x = self.output(x)
        return x