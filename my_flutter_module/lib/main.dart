import 'package:flutter/material.dart';
import 'package:my_flutter_module/AddTaskScreen.dart';
import 'package:my_flutter_module/TaskDetailsScreen.dart';

void main() {
  runApp(const MyApp());
}


class Task {
  int id;
  String title;
  String description;
  bool isComplete;

  Task(this.id, this.title, this.description, {this.isComplete = false});
}

class TaskService {
  List<Task> tasks = [];

  void addTask(String title, String desc) {
    int id = tasks.length + 1;
    tasks.add(Task(id, title, desc));
  }

  void toggleDone(int id) {
    for (var t in tasks) {
      if (t.id == id) {
        t.isComplete = !t.isComplete;
      }
    }
  }

  void deleteTask(int id) {
    tasks.removeWhere((task) => task.id == id);
  }

  Task? getTask(int id) {
    for (var task in tasks) {
      if (task.id == id) {
        return task;
      }
    }
    return null;
  }
}

TaskService service = TaskService();

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Todo List", style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.purple,
      ),
      body: ListView(
        children: service.tasks.map((task) {
          return ListTile(
            leading: Checkbox(
              value: task.isComplete,
              onChanged: (_) => setState(() {
                service.toggleDone(task.id);
              }),
            ),
            title: Text(task.title),
            subtitle: Text(task.description),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => TaskDetailsScreen(task: task),
                ),
              ).then((_) => setState(() {}));
            },

          );
        }).toList(),
      ),
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const AddTaskScreen()),
          ).then((_) => setState(() {}));
        },
      ),
    );
  }
}
