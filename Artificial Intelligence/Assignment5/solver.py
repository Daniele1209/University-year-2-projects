# -*- coding: utf-8 -*-
"""
In this file your task is to write the solver function!

"""


def solver(t, w):
    """
    Parameters
    ----------
    t : TYPE: float
        DESCRIPTION: the angle theta
    w : TYPE: float
        DESCRIPTION: the angular speed omega

    Returns
    -------
    F : TYPE: float
        DESCRIPTION: the force that must be applied to the cart
    or
    
    None :if we have a division by zero

    """
    theta = {"NVB": [-50, -25, -40], "NB": [-40, -10, -25], "N": [-20, 0, -10], "ZO": [-5, 5, 0], "P": [0, 20, 10],
             "PB": [10, 40, 25], "PVB": [25, 50, 40]}

    membershipDegrees_theta = get_membershipDegrees(theta, t)

    omega = {"NB": [-10, -3, -8], "N": [-6, 0, -3], "ZO": [-1, 1, 0], "P": [0, 6, 3], "PB": [3, 10, 8]}

    membershipDegrees_omega = get_membershipDegrees(omega, w)

    # rules for the fuzzy system
    force_table = {"PVB PB": "PVVB", "PVB P": "PVVB", "PVB ZO": "PVB", "PVB N": "PB", "PVB NB": "P",
                   "PB PB": "PVVB", "PB P": "PVB", "PB ZO": "PB", "PB N": "P", "PB NB": "Z",
                   "P PB": "PVB", "P P": "PB", "P ZO": "P", "P N": "Z", "P NB": "N",
                   "ZO PB": "PB", "ZO P": "P", "ZO ZO": "Z", "ZO N": "N", "ZO NB": "NB",
                   "N PB": "P", "N P": "Z", "N ZO": "N", "N N": "NB", "N NB": "NVB",
                   "NB PB": "Z", "NB P": "N", "NB ZO": "NB", "NB N": "NVB", "NB NB": "NVVB",
                   "NVB PB": "N", "NVB P": "NB", "NVB ZO": "NVB", "NVB N": "NVVB", "NVB NB": "NVVB"
                   }

    forces = {}
    for val1 in theta:
        for val2 in omega:
            val = min(membershipDegrees_theta[val1], membershipDegrees_omega[val2])
            new_key = val1 + " " + val2
            if force_table[new_key] not in forces:
                forces[force_table[new_key]] = val
            elif forces[force_table[new_key]] < val:
                forces[force_table[new_key]] = val

    products = {'NVVB': -32, 'NVB': -24, 'NB': -16, 'N': -8, 'Z': 0, 'P': 8, 'PB': 16, 'PVB': 24, 'PVVB': 32}

    sum = 0
    product = 0
    for force in forces:
        sum += forces[force]
        product += forces[force] * products[force]

    if sum != 0:
        f = product / sum
    else:
        f = 0

    return f


def get_membershipDegrees(values, val):
    membershipDegrees = {}
    for item in values:
        membershipDegrees[item] = 0
        if values[item][0] <= val <= values[item][1]:
            membershipDegrees[item] = max(0, min((val - values[item][0]) / (values[item][2] - values[item][0]), 1,
                                                 (values[item][1] - val) / (values[item][1] - values[item][2])))

    return membershipDegrees
