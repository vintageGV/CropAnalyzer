import tensorflow as tf, sys

image_path = sys.argv[1]

# Read in the image_data
image_data = tf.gfile.FastGFile(image_path, 'rb').read()

# Loads label file, strips off carriage return
import os
curdir = os.getcwd()
label_lines = [line.rstrip() for line 
                   in tf.gfile.GFile(curdir + "/resources/ANN/training/retrained_labels.txt")]

# Unpersists graph from filee
with tf.gfile.FastGFile(curdir + "/resources/ANN/training/retrained_graph.pb", 'rb') as f:
    graph_def = tf.GraphDef()
    graph_def.ParseFromString(f.read())
    _ = tf.import_graph_def(graph_def, name='')

with tf.Session() as sess:
    # Feed the image_data as input to the graph and get first prediction
    softmax_tensor = sess.graph.get_tensor_by_name('final_result:0')
    
    predictions = sess.run(softmax_tensor, \
             {'DecodeJpeg/contents:0': image_data})
    
    # Sort to show labels of first prediction in order of confidence
    top_k = predictions[0].argsort()[-len(predictions[0]):][::-1]

    for node_id in top_k:
        human_string = label_lines[node_id]
        score = predictions[0][node_id]
        print('\t%s\t\t%.2f%s' % (human_string, score * 100, '%'))

    objname = label_lines[top_k[0]]
    print('\tstate:' + objname)


