# Generated by Django 2.1.7 on 2019-04-03 00:12

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('food', '0012_water'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='water',
            name='isDanger',
        ),
        migrations.AlterField(
            model_name='water',
            name='calories_gained',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='water',
            name='name',
            field=models.CharField(default='Water', max_length=50),
        ),
    ]
