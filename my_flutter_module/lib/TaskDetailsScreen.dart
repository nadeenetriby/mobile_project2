import 'package:flutter/material.dart';
import 'package:my_flutter_module/main.dart';

class TaskDetailsScreen extends StatelessWidget {
  final Task task;
  const TaskDetailsScreen({super.key, required this.task});

  @override
  Widget build(BuildContext context) {
    final service = TaskService();

    return Scaffold(
      appBar: AppBar(
        title: const Text("Task Details", style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.purple,
      ),
      body: Align(
        alignment: Alignment.center,
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            children: [
              Text("Title: ${task.title}", style: const TextStyle(fontSize: 20)),
              const SizedBox(height: 10),
            Text("Description: ${task.description}", style: const TextStyle(fontSize: 16)),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                service.deleteTask(task.id);
                Navigator.pop(context);
              },
              child: const Text("Delete Task"),
            ),
          ],
        ),
      ),
      ),
    );
  }
}