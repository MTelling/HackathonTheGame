var packageJSON = require('./package.json');
var path = require('path');
var webpack = require('webpack');
// var webpack = require('./node_modules/.bin/webpack');
var ROOT = path.resolve(__dirname, 'src/main/resources/static');
var SRC = path.resolve(ROOT, 'js');
var DEST = path.resolve(__dirname, 'src/main/resources/static/dist');

const PATHS = {
  build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'webjars', packageJSON.name, packageJSON.version)
};

module.exports = {
    entry: {
      app: SRC + '/index.jsx',
    },
    devtool: 'source-map',
    output: {
        path: DEST,
        filename: 'app-bundle.js',
        publicPath: '/dist/',
    },
    module: {
      loaders: [
        {
          test: /\.jsx?$/,  // Notice the regex here. We're matching on js and jsx files.
          loaders: 'babel-loader',
          query: {
            presets: ['es2015', 'react']
          },
          include: SRC
        },

        {test: /\.css$/, loader: 'style-loader!css-loader'},
        {test: /\.less$/, loader: 'style!css!less'},

        // Needed for the css-loader when [bootstrap-webpack](https://github.com/bline/bootstrap-webpack)
        // loads bootstrap's css.
        {test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&amp;mimetype=application/font-woff'},
        {test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&amp;mimetype=application/octet-stream'},
        {test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file'},
        {test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&amp;mimetype=image/svg+xml'}
      ],
    },
    resolve: {
      // alias: [
      //   path.resolve(ROOT, 'js'),
      //   path.resolve(ROOT, 'css'),
      // ],
      extensions: ['*', '.js', '.jsx']
    },
    plugins: [new webpack.HotModuleReplacementPlugin(), new webpack.NoEmitOnErrorsPlugin()]
};
