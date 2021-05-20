import torch
import torch.nn.functional as F
import matplotlib.pyplot as plt
import numpy as np
from tqdm import tqdm
import myModel

data = torch.load('mydataset.dat')
device = torch.device("cuda:0")

x1_points, x2_points, f_tensor = torch.load("mydataset.dat")
variables = torch.stack((x1_points, x2_points), dim=1).to(device)
f_tensor = f_tensor.unsqueeze(1)

# we set up the lossFunction as the mean square error
lossFunction = torch.nn.MSELoss()

# we create the ANN
ann = myModel.Net(n_feature=2, n_hidden=10, n_output=1)

print(ann)
# we use an optimizer that implements stochastic gradient descent
optimizer_batch = torch.optim.SGD(ann.parameters(), lr=0.002)


# we memorize the losses for some graphics
loss_list = []
avg_loss_list = []

# we set up the environment for training in batches
batch_size = 32
n_batches = int(len(f_tensor) / batch_size)
print("Batch number: " + str(n_batches))

for epoch in tqdm(range(100000)):

    for batch in range(n_batches):
        # we prepare the current batch  -- please observe the slicing for tensors
        batch_X = variables[batch * batch_size:(batch + 1) * batch_size, ]
        batch_y = f_tensor[batch * batch_size:(batch + 1) * batch_size, ]

        batch_X = batch_X.to(device)
        batch_y = batch_y.to(device)

        # we compute the output for this batch
        prediction = ann(batch_X)

        # we compute the loss for this batch
        loss = lossFunction(prediction, batch_y)

        # we save it for graphics
        loss_list.append(loss.item())

        # we set up the gradients for the weights to zero (important in pytorch)
        optimizer_batch.zero_grad()

        # we compute automatically the variation for each weight (and bias) of the network
        loss.backward()

        # we compute the new values for the weights
        optimizer_batch.step()

    if epoch % 250 == 0:

        y_pred = ann(variables).to(device)
        f_tensor = f_tensor.to(device)
        loss = lossFunction(y_pred, f_tensor)
        print('\repoch: {}\tLoss =  {:.5f}'.format(epoch, loss.item()))

    # Specify a path
filepath = "myNetwork.pt"

# save the model to file
torch.save(ann.state_dict(), filepath)
print(loss_list)
# visualise the parameters for the ann (aka weights and biases)
for name, param in ann.named_parameters():
    if param.requires_grad:
        print(name, param.data)


plt.plot(loss_list)
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.show()