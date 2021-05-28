#create function
import pandas as pd
from os.path import dirname,join

def plus(parameter1,parameter2):
    return parameter1 + parameter2
def multipy(parameter1,parameter2):
    return parameter1 * parameter2

def read():
    filename = join(dirname(__file__),"remake_darktour.csv")
    darktour = pd.read_csv(filename)
    return darktour
