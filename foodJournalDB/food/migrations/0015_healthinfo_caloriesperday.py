# Generated by Django 2.1.7 on 2019-04-05 05:49

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('food', '0014_healthcondition'),
    ]

    operations = [
        migrations.AddField(
            model_name='healthinfo',
            name='caloriesPerDay',
            field=models.IntegerField(default=2000, null=True),
        ),
    ]
