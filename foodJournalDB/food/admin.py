# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin
from .models import Account, HealthInfo, Food, Exercise, DangerFood, Water, HealthCondition

admin.site.register(Account)
admin.site.register(HealthInfo)
admin.site.register(Food)
admin.site.register(Exercise)
admin.site.register(DangerFood)
admin.site.register(Water)
admin.site.register(HealthCondition)
