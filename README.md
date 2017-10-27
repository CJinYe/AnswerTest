#答题应用
1:需要传递文件路径,name为"FilePath"
2:需要传递文件名,name为"FileName"

Intent intent = new Intent();
ComponentName componentName = new ComponentName("com.icox.exercises",
          "com.icox.exercises.activity.MainActivity");
intent.setComponent(componentName);
intent.putExtra("FilePath", "test");
intent.putExtra("FileName", "test");
startActivity(intent);