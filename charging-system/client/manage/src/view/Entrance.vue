<template>
  <div
    id="bhe_entrance"
    style="overflow-y: hidden; overflow-x: hidden; display: flex; background-color: #f1f1f1;"
  >
    <div id="bhe_left">
      <img src="../assets/entrance.png" style="display: block; height: 100vh; min-height: 800px;">
    </div>
    <div id="bhe_right" style="position: relative; min-width: 600px; width: 100%;">
      <router-view/>
    </div>
  </div>
</template>
