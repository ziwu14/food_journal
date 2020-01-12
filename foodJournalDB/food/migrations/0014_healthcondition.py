# Generated by Django 2.1.7 on 2019-04-03 00:24

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('food', '0013_auto_20190403_0012'),
    ]

    operations = [
        migrations.CreateModel(
            name='HealthCondition',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('description', models.CharField(default='None', max_length=2000)),
                ('account', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='food.Account')),
            ],
        ),
    ]
