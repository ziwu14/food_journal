# Generated by Django 2.1.7 on 2019-04-09 21:54

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('food', '0016_auto_20190405_0728'),
    ]

    operations = [
        migrations.AddField(
            model_name='exercise',
            name='length',
            field=models.IntegerField(default=0, null=True),
        ),
    ]