import 'package:flutter/material.dart';
import 'package:my_flutter_module/main.dart';

class AddTaskScreen extends StatefulWidget {
  const AddTaskScreen({super.key});

  @override
  State<AddTaskScreen> createState() => _AddTaskScreenState();
}

class _AddTaskScreenState extends State<AddTaskScreen> {
  final titleCtrl = TextEditingController();
  final descCtrl = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Add Task", style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.purple,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(controller: titleCtrl, decoration: const InputDecoration(hintText: "Title")),
            TextField(controller: descCtrl, decoration: const InputDecoration(hintText: "Description")),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                service.addTask(titleCtrl.text, descCtrl.text);
                Navigator.pop(context);
              },
              child: const Text("Add Task"),
            )
          ],
        ),
      ),
    );
  }
}