# Generated by Django 2.1.7 on 2019-04-02 23:44

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('food', '0010_auto_20190402_2317'),
    ]

    operations = [
        migrations.AddField(
            model_name='food',
            name='isDanger',
            field=models.BooleanField(default=False),
        ),
    ]
