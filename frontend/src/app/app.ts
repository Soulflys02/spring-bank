import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterOutlet } from '@angular/router';
import Keycloak from 'keycloak-js';

@Component({
  imports: [RouterOutlet, MatToolbar, MatButton],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  private readonly keycloak = inject(Keycloak);
  protected readonly userName = this.keycloak.tokenParsed?.['name'];

  protected logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }
}
