var Canvas = function(id, n) {

  var canvas = document.getElementById(id);

  this.context = canvas.getContext("2d");
  this.width = canvas.width;
  this.height = canvas.height;
  this.square = {
    width: this.width / n,
    height: this.height / n,
  }

  this.initializeStyle();
  this.startGrid();
}

Canvas.prototype = {

  initializeStyle: function() {
    this.context.translate(0.5, 0.5)
    this.context.strokeStyle = "#000";
    this.context.fillStyle = "#000";
  },

  black: function() {
    this.context.strokeStyle = "#000";
    this.context.fillStyle = "#000";
  },

  blue: function() {
    this.context.strokeStyle = "#0090a3";
    this.context.fillStyle = "#0090a3";
  },

  startGrid: function() {
    for (i = 0; i < this.width; i += this.square.width) {
      for (j = 0; j < this.height; j += this.square.height)
        this.draw(i, j);
    }
  },

  draw(x, y) {
    this.context.rect(x, y, this.square.width, this.square.height);
    this.context.stroke();
  },

  fill(x, y) {
    this.context.fillRect(x, y, this.square.width, this.square.height);
  },

  open(x, y) {
    x--;
    y--;
    y = y * this.square.width;
    x = x * this.square.height;
    this.draw(y, x);
    this.fill(y, x);
  }

}


var UF = function(n) {
  this.id = [];
  for (var i = 0; i < n; i++) {
    this.id[i] = i;
  }
}

UF.prototype = {

  root: function(i) {
    while (i != this.id[i]) {
      this.id[i] = this.id[this.id[i]];
      i = this.id[i];
    }
    return i;
  },

  connected: function(p, q) {
    return this.root(p) == this.root(q);
  },

  union: function(p, q) {
    var i = this.root(p);
    var j = this.root(q);
    if (i == j) {
      return;
    }
    this.id[i] = j;
  }

}

var Percolation = function(n) {
  this.n = n;
  this.grid = [];
  for (var i = 0; i < n + 2; i++) {
    this.grid[i] = [];
    for (var j = 0; j < n + 2; j++) {
      this.grid[i][j] = 0;
    }
  }
  this.unionFind = new UF((n * n) + 2);
  this.unionFindWithoutBottom = new UF((n * n) + 1);
  this.top = 0;
  this.bottom = (n * n) + 1;
  this.openSites = 0;
}

Percolation.prototype = {

  union: function(x, y, uf) {
    if (!uf.connected(x, y)) {
      uf.union(x, y);
    }
  },

  unionTop: function(row, col) {
    if (row == 1) {
      this.union(col, this.top, this.unionFind);
      this.union(col, this.top, this.unionFindWithoutBottom);
    }
  },

  unionBottom: function(row, col) {
    if (row == this.n) {
      this.union(this.equivalent(row, col), this.bottom, this.unionFind);
    }
  },

  equivalent: function(row, col) {
    return ((row - 1) * this.n) + col;
  },

  unionBounds: function(row, col) {
    if (this.grid[row - 1][col] == 1) {
      this.union(this.equivalent(row, col), this.equivalent(row - 1, col), this.unionFind);
      this.union(this.equivalent(row, col), this.equivalent(row - 1, col), this.unionFindWithoutBottom);
    }
    if (this.grid[row][col + 1] == 1) {
      this.union(this.equivalent(row, col), this.equivalent(row, col + 1), this.unionFind);
      this.union(this.equivalent(row, col), this.equivalent(row, col + 1), this.unionFindWithoutBottom);
    }
    if (this.grid[row + 1][col] == 1) {
      this.union(this.equivalent(row, col), this.equivalent(row + 1, col), this.unionFind);
      this.union(this.equivalent(row, col), this.equivalent(row + 1, col), this.unionFindWithoutBottom);
    }
    if (this.grid[row][col - 1] == 1) {
      this.union(this.equivalent(row, col), this.equivalent(row, col - 1), this.unionFind);
      this.union(this.equivalent(row, col), this.equivalent(row, col - 1), this.unionFindWithoutBottom);
    }
  },

  open: function(row, col) {
    if (!this.isOpen(row, col)) {
      this.grid[row][col] = 1;
      this.unionTop(row, col);
      this.unionBottom(row, col);
      this.unionBounds(row, col);
      this.openSites++;
    }
  },

  isOpen: function(row, col) {
    return (this.grid[row][col] == 1);
  },

  isFull: function(row, col) {
    return this.unionFindWithoutBottom.connected(this.equivalent(row, col), this.top);
  },

  percolates: function() {
    return this.unionFind.connected(this.top, this.bottom);
  }


}

var percolation = new Percolation(5000);
do {
  do {
    var row = Math.floor(Math.random() * percolation.n) + 1;
    var col = Math.floor(Math.random() * percolation.n) + 1;
  }
  while (percolation.isOpen(row, col));
  percolation.open(row, col);
} while(!percolation.percolates());
alert('finish ' + percolation.openSites);
