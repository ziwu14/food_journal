from django.conf.urls import url
from . import views

# The following url patterns direct the server request to which view to access (in views.py)
#The names are self explanatory, and match the names of the functions in views.py
urlpatterns = [
    #url(r'^(?P<type>[a-zA-z]+)/(?P<name>[a-zA-z]+)/$', views.detail, name='detail'),
    url(r'^account/$', views.account, name='account'),
    url(r'^authentication/$', views.login_authentication, name='login_authentication'),
    url(r'^health_info/$', views.healthinfo, name='healthinfo'),
    url(r'^food/$', views.food, name='food'),
    url(r'^exercise/$', views.exercise, name='exercise'),
    url(r'^danger_food/$', views.dangerFood, name='danger_food'),
    url(r'^water/$', views.water, name='water'),
    url(r'^health_condition/$', views.healthCondition, name='health_condition'),
]
