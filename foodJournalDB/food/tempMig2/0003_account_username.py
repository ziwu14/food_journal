# Generated by Django 2.1.7 on 2019-03-02 23:10

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('food', '0002_auto_20190302_2310'),
    ]

    operations = [
        migrations.AddField(
            model_name='account',
            name='username',
            field=models.CharField(max_length=200, null=True),
        ),
    ]