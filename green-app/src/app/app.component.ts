import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {UserinfoComponent} from "./userinfo/userinfo.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, UserinfoComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'green-app';
}
